FROM mcr.microsoft.com/dotnet/sdk:5.0

WORKDIR /app
COPY . .
WORKDIR /app/hello-world

CMD dotnet run --project hello
