#!/bin/sh

docker compose down

docker compose up -d
while [[ $(docker-compose ps --services --filter "status=running" | wc -l) -ne $(docker-compose ps --services | wc -l) ]]; do
    echo "Aguardando todos os serviços iniciarem..."
    sleep 5
done

LOCALSTACK_ENDPOINT=http://localhost:4566
AWS_REGION=us-east-1
DLQ_NAME=queue_dlq_client
QUEUE_NAME=queue_client

echo "CRIANDO ${DLQ_NAME}..."
aws --endpoint-url=$LOCALSTACK_ENDPOINT sqs create-queue --queue-name $DLQ_NAME --region $AWS_REGION --attributes VisibilityTimeout=30

DLQ_ARN=$(aws --endpoint-url=$LOCALSTACK_ENDPOINT sqs get-queue-attributes --queue-url $LOCALSTACK_ENDPOINT/000000000000/$DLQ_NAME --attribute-name QueueArn --query 'Attributes.QueueArn' --output text)

echo "CRIANDO ${QUEUE_NAME}..."
echo "NUMERO MAXIMO DE RETRIES: 3"
aws --endpoint-url=$LOCALSTACK_ENDPOINT sqs create-queue --queue-name $QUEUE_NAME --attributes '{"RedrivePolicy": "{\"deadLetterTargetArn\":\"$DLQ_ARN\",\"maxReceiveCount\":\"3\"}", "VisibilityTimeout": "30"}'

