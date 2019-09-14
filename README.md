
# Payment Service Code Challenge

* You are tasked to build a payment service that provides the following operations along with some statistics.

### Payment Operations

1. **Create**
     * A payment cannot be created if:
         * The source account is equal to the destination.
         * The amount is negative.
     * A Payment is created with *CREATED* state.
     * Created payments can move to *EXECUTED*, *REJECTED* or *CANCELED*

2. **Execute**
     * If the source account doesn't have enough balance, the state must be changed to *REJECTED*.
     * Rejected payments cannot move to any state.
     * If the balance is sufficient, then:
         * A transaction should be created on the source account with a negative amount.
         * A transaction should be created on the destination account with a positive amount.
         * The state must be changed to *EXECUTED*.
     * Executed payments cannot move to any state.

3. **Cancel**
     * Canceling a payment will change it's state to *CANCELED*.
     * Cancelled payments cannot move to any state.


### Payment Endpoint
* Get: `/api/v1/payment/{id}`
* Create: `/api/v1/payment`
* Cancel: `/api/v1/payment/cancel/{id}`
* Execute: `/api/v1/payment/execute/{id}`
* Note: check `CreatePayment` class which you can use as a request body.

### Statistics Endpoint - Optional

* Add an endpoint `/api/v1/statistics` which has one GET API that takes one path parameter (second).
* The 'second' parameter will specify the period in which to get the statistics for, see the example below.
* The 'second' parameter max value is 10.
* The code that will return statistics has to be O(1) (constant time) execution time.
* Statistics is basically a map of account id and a statistic instance for that account.
* Only transactions with amount greater than zero should be included.
* If an account has negative transactions only, then it should be excluded.


* Every statistic instance represents the following:
    * String accountId: the account id.
    * Double maxTrans: which is the maximum amount of all the transactions.
    * Double minTrans: which is the minimum amount of all the transactions.
    * Double avgTrans: which is the average amount of all the transactions.
    * List transactions: all the positive transactions of the account.
   
   
* For instance: assuming that the following accounts had the following transactions.
    * Account1:
        * Transaction1 - amount: 10, created: 10:05:01
        * Transaction2 - amount: 20, created: 10:05:02
        * Transaction3 - amount: 30, created: 10:05:03
        * Transaction4 - amount: 40, created: 10:05:04
    * Account2:
        * Transaction1 - amount: 100, created: 10:05:01
        * Transaction2 - amount: 200, created: 10:05:02
        * Transaction3 - amount: 300, created: 10:05:03
        * Transaction4 - amount: 400, created: 10:05:04
    
    
 * When a request is made to '/api/v1/statistics/2', assuming the time is: 10:05:05 (period of the last 2 seconds).
     * Result:
         * Account1:
               * Max: 40
               * Min: 30
               * Avg: 35
               * Transactions:
                   * Transaction3 - amount: 30, created: 10:05:03
                   * Transaction4 - amount: 40, created: 10:05:04
         * Account2:
             * Max: 400
             * Min: 300
             * Avg: 350
             * Transactions:
                 * Transaction3 - amount: 300, created: 10:05:03
                 * Transaction4 - amount: 400, created: 10:05:04


### Expectations

* Clean code practices.
* Code should be well tested.
* It's highly encouraged to push commits regularly rather than one fat commit.


### Considerations

* Integration test has to pass.
* Obviously integration test must not be changed. (Except uncommenting scenario 10).
* If you implement statistics make sure to uncomment scenario 10.


### Notes

* Please be aware that the code has some bad practices 
  that are meant for convenience only and should not be followed.