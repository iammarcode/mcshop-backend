#!/bin/bash

# env variables
aws_region="ap-east-1"

# create sns topic
awslocal sns create-topic --name customer-otp-topic
awslocal sns create-topic --name customer-register-topic

# create sqs
awslocal sqs create-queue --queue-name customer-otp-queue
awslocal sqs create-queue --queue-name customer-register-queue

# create sns-sqs subscriptions
awslocal sns subscribe --topic-arn arn:aws:sns:"${aws_region}":000000000000:customer-otp-topic --protocol sqs --notification-endpoint arn:aws:sqs:"${aws_region}":000000000000:customer-otp-queue
awslocal sns subscribe --topic-arn arn:aws:sns:"${aws_region}":000000000000:customer-register-topic --protocol sqs --notification-endpoint arn:aws:sqs:"${aws_region}":000000000000:customer-register-queue
