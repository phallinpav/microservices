#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base
WORKDIR /app
EXPOSE 80

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["message-service/message-service.csproj", "message-service/"]
RUN dotnet restore "message-service/message-service.csproj"
COPY . .
WORKDIR "/src/message-service"
RUN dotnet build "message-service.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "message-service.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "message-service.dll"]