using message_service.Model;
using message_service.Service;
using Microsoft.AspNetCore.Mvc;

namespace message_service.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class ChatController : ControllerBase
    {
        private static readonly string _apiKeyHeader = "x-api-key";

        private readonly IAccountService _accountService;
        private readonly IChatService _chatService;

        public ChatController(IChatService chatService, IAccountService accountService)
        {
            _chatService = chatService;
            _accountService = accountService;
        }

        [HttpGet("{toAccId}")]
        public async Task<IActionResult> GetChats(long toAccId, [FromQuery] int? limit)
        {
            if (Request.Headers.TryGetValue(_apiKeyHeader, out var apiKeyValue))
            {
                long accId;
                try
                {
                    accId = await _accountService.GetAccountId(apiKeyValue);
                    if (!await _accountService.IsChattable(apiKeyValue, toAccId))
                    {
                        return BadRequest("target account does not exist");
                    }
                }
                catch (Exception)
                {
                    return BadRequest("invalid x-api-key value");
                }
                return Ok(_chatService.GetChats(accId, toAccId, limit.HasValue ? limit.Value : 20));
            }
            else
            {
                return BadRequest("require header 'x-api-key'");
            }
        }

        [HttpPost("{toAccId}")]
        public async Task<IActionResult> Chat(long toAccId, [FromBody] ChatRequest body)
        {
            if (Request.Headers.TryGetValue(_apiKeyHeader, out var apiKeyValue))
            {
                if (body.Message.Length == 0)
                {
                    return BadRequest("empty message");
                }

                long accId;
                try
                {
                    accId = await _accountService.GetAccountId(apiKeyValue);
                    if (!await _accountService.IsChattable(apiKeyValue, toAccId))
                    {
                        return BadRequest("target account does not exist");
                    }
                } catch (Exception)
                {
                    return BadRequest("invalid x-api-key value");
                }
                _chatService.ChatTo(accId, toAccId, body.Message);
                return Ok();
            } else
            {
                return BadRequest("require header 'x-api-key'");
            }
        }
    }
}
