# API Structure

##Scenario
Assess Credit Risk for customer depending on customer id and requests loan amount.
- Response will be either A, B, or JUNK
- Flow and Flux artificially send multiple responses at intervals
    - output is streamed to client

##Use cases 
- Use case 1 : Executable Commands and Ports
- Use case 2 : Self Executing Commands and Ports
- Use case 3 : CommandExecutorService
- Use case 4 : Reactive Mono Response Stream
- Use case 5 : Reactive Flux Response Stream
- Use case 6 : Coroutine (suspend)
- Use case 7 : Coroutine (Flow)

##Use cases apply to
- Ingress
- Command (Business)
- Port