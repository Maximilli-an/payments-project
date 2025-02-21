Hi, Max Nash here building a payment project based on previously provided instructions

Initial thoughts
Java 11+, Springboot, React Frontend (if I get time) - I am conscious this is a backend role so i'd rather have a strong showing on the backend including tests

Swagger OpenAPI - something I haven't done before - this will be learning

DB type - comfort pick is MongoDB, but for data like this MySQL is probably better for things like Zip codes likely to be extended to include location based relationships, card number and cvc and expiry likely to be relational as well

Webhook -> pass details as JSON in body (responsebody) - needs resiliency (error handling, maybe rate limitation?)

at least one custom validation - carb number must be num and 12 length for example, firstname/lastname must be alphanumeric with dashes and apostraphe's allowed.

4 hours average

Backend setup Api x 2, Testing Junit and Integration, SQL query and schema + setup, ENCRYPTION

Going to use Spring Initializr for simplicity of dependancy config to begin, JPA, Spring Sec, Springboot Web MVC, Lombok for readability/annotations, MySQL driver

File structure will be something like api, model, repo, interface

Start from Data Model point
create Payment data shape
- will be going with MySQL overall - reason for this is because things like zipCode can be relationally relevant if we choose to include more 'location specific' validations - country, state, city, etc can reference zipcode for validation.
- give unique identifier for each process, this will help with logging extendability in future.
- went with UUID as i've seen it more often than incremental id's
- no nullable fields
- createdAt similar to UUID - helps with logging and monitoring

make sure card number is encrypted before it hits DB
we aren't storing the 'amount paid' at all - I wonder if this is a compliance thing? Probably.

Moving to the JPA Repo,
As a customer I would want to be able to search my own data back using my name and card number (zipcode may have cross overs)
I would actually have broader identifiers or a JWT for auth to request this

Moving to business logic / service.
we inject PaymentRepo so we have access to .save etc.
Need to create an encryptor util for use here
I want to create a re-usable encryption method, we will use this in saving initially, but extension possibilities mean having this re-usable would be nicer.

Need to pause here before completing encryption - do some encryption options research and other work.

Changed my approach after some small research and thinking, keen to go with a Hashed credit card number since we will never need to decrypt (at this iteration)

Refactoring the payment model as I want the input vs the saved to be different, take cardNum as a transient property, hash it, then save the hash to the DB.

I'm choosing not to handle optionals for any field, all fields from the frontend will be required.

Moving ahead to the actual api invocation, i went for a Try/Catch immediately with a ResponseEntity, however - this made me realise I need a DTO struct for strictness to the typing
So creating the DTO shape now.

This may need to shift once I add in validations which im planning on doing.

Used GPT For Regex help on creating validators, prompt link: https://chatgpt.com/share/67b5ad31-3278-8008-bda2-c429167d6979

I am thinking about Explicit true/false handling of validations versus throwing exceptions first - from research throwing exceptions seems to be the preferred pattern so I will do it this way to avoid manual checks in PaymentService and maintain an exception handling style.

okay happy with validators now.

will do some quick tests before moving to the frontend form.

Had to tweak a lot of stuff for MYSQL connection - this really showed my less experience with mySQL but got it all working in the end!

Haven't set up OpenAPI from scratch before so will be doing that, then tests, then frontend

GPT Link for setting up OpenAPI docs:
https://chatgpt.com/share/67b81189-681c-8008-9a85-ff424d891705

When writing tests, especially for PaymentAPI I considered full integration or just API behaviour testing
Upon research I realised i'd need to configure h2 DB for testing and this is unfamiliar to me, so I chose to opt for pure API behaviour testing only.

Added a zipCode test and validator to cover off edge cases / unhandled zipCode validation (very basic).

Moving all this to a new repo before attempting to initialize the frontend




Moved Extension thoughts here
    //Extension opportunity for customers requesting what payments they have made
    //Custom Search First, Last and EncryptedCardNumber based
    //List<Payment> findByFirstLastAndCardNumber(String firstName, String lastName, String encryptedCardNumber);


    //extension option for search from customer
    //public List<Payment> findPayments(String firstName, String lastName, String cardNumber) {
    // new validator?
    //String hashedCard = hash(cardNumber);
    //return paymentRepository.findByFirstLastAndCardNumber(
    //firstName, lastName, hashedCard);
    //}
