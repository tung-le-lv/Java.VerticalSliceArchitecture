package com.openmind.order.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * Replaces ServiceCollectionExtensions.AddCoreServices/AddEventBus. All three
 * clients honor AWS_ENDPOINT_URL / AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY
 * explicitly (rather than relying on the SDK's default credential/endpoint
 * resolution) because those only need to be Spring properties here -- e.g. set
 * via a Spring profile file instead of real OS environment variables -- and the
 * SDK's own provider chain only reads actual env vars/system properties. SNS
 * and SQS additionally branch on USE_LOCAL_EVENT_BUS to target LocalStack.
 *
 * All three clients set the region explicitly from AWS_DEFAULT_REGION: unlike
 * the .NET SDK (and the AWS CLI/boto3), the Java SDK v2's default region
 * provider chain only recognizes AWS_REGION, not AWS_DEFAULT_REGION -- and this
 * project's docker-compose.yaml only sets the latter, so leaving region
 * resolution implicit fails client construction entirely.
 */
@Configuration
public class AwsClientConfig
{
    @Bean
    public DynamoDbClient dynamoDbClient(@Value("${AWS_ENDPOINT_URL:}") String endpointUrl,
            @Value("${AWS_DEFAULT_REGION:ap-southeast-2}") String region,
            @Value("${AWS_ACCESS_KEY_ID:}") String accessKeyId,
            @Value("${AWS_SECRET_ACCESS_KEY:}") String secretAccessKey)
    {
        var builder = DynamoDbClient.builder().region(Region.of(region));
        if (endpointUrl != null && !endpointUrl.isBlank())
        {
            builder.endpointOverride(URI.create(endpointUrl));
        }
        if (!accessKeyId.isBlank() && !secretAccessKey.isBlank())
        {
            builder.credentialsProvider(
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)));
        }
        return builder.build();
    }

    @Bean
    public SnsClient snsClient(@Value("${USE_LOCAL_EVENT_BUS:false}") boolean useLocalEventBus,
            @Value("${LOCALSTACK_ENDPOINT:http://localhost:4566}") String localstackEndpoint,
            @Value("${AWS_DEFAULT_REGION:ap-southeast-2}") String region)
    {
        var builder = SnsClient.builder().region(Region.of(region));
        if (useLocalEventBus)
        {
            builder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                    .endpointOverride(URI.create(localstackEndpoint));
        }
        return builder.build();
    }

    @Bean
    public SqsClient sqsClient(@Value("${USE_LOCAL_EVENT_BUS:false}") boolean useLocalEventBus,
            @Value("${LOCALSTACK_ENDPOINT:http://localhost:4566}") String localstackEndpoint,
            @Value("${AWS_DEFAULT_REGION:ap-southeast-2}") String region)
    {
        var builder = SqsClient.builder().region(Region.of(region));
        if (useLocalEventBus)
        {
            builder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                    .endpointOverride(URI.create(localstackEndpoint));
        }
        return builder.build();
    }
}
