#!/bin/bash

curl -X GET http://127.0.0.1:60968/api/v1/auth/otp \
  -H "Content-Type: application/json" \
  -d '{"email": "workmarcohk@gmail.com"}'

curl -X POST http://127.0.0.1:60968/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"workmarcohk@gmail.com","password":"1234","username":"superman","otp":399534}'

curl -X GET http://127.0.0.1:60968/api/v1/user/profile/me -H "Authorization: Bearer <your-jwt-token>"