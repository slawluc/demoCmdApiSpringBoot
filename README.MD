# API Structure

## Scenario
Assess Credit Risk for customer depending on customer id and requests loan amount.
- Response will be either A, B, or JUNK
    - Loan amount 1 to 1000 is a small loan assessment system check
    - Loan amount 1001 to 10,000 is a medium loan assessment system check
    - Loan amount 10,001 to 100,000 is a large loan assessment system check
- Flow and Flux artificially send multiple responses at intervals
    - output is streamed to client

## Use cases 
- Use case 1 : Executable Commands and Ports
- Use case 2 : Self Executing Commands and Ports
- Use case 3 : CommandExecutorService
- Use case 4 : Reactive Mono Response Stream
- Use case 5 : Reactive Flux Response Stream
- Use case 6 : Coroutine (suspend)
- Use case 7 : Coroutine (Flow)

## Use cases apply to packages;
- Ingress
- Command (Business)
- Port
- Framework
    - types that belong in supporting libs only
    
## Usage
Set `active.profiles` argument to any of the following;
- UseCase1
- UseCase2
- UseCase3
- UseCase4
- UseCase5
- UseCase6
- UseCase7


### cURL
See ServerTests.htpp or the following bash script (especially for SSE use cases)
```bash
curl -X POST \
  http://localhost:8080/risk \
  -H 'content-type: application/json' \
  -H 'x-channel-id: WEB' \
  -H 'x-trust-token: 123456' \
  -d '{
"customerId" : "123456",
"amount" : 1001,
"currency" : "USD"
}'
```