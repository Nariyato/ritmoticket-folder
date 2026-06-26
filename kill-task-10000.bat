netstat -ano | findstr LISTENING | findstr :10000
taskkill /F /PID <PID>