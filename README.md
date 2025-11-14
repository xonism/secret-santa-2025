# Secret Santa

An endpoint to email each participant with the name of their Secret Santa victim.

### Environment variables

`MAIL_USERNAME`
Google email address that you want to send emails from.

`MAIL_PASSWORD`
Google app password.
https://support.google.com/mail/answer/185833

### Request body example

{
    "participants": [
        {
            "name": "Harrier",
            "emailAddress": "harrier@example.com"
        },
        {
            "name": "Kim",
            "emailAddress": "kim@example.com"
        },
        {
            "name": "Cuno",
            "emailAddress": "cuno@example.com"
        },
        {
            "name": "Measurehead",
            "emailAddress": "measurehead@example.com"
        }
    ]
}
