# Spring Boot Reactive Demo

This is a demo project to add Elastic Observability step-by-step.

## Get up and running

### Start Elasticsearch

Start an Elasticsearch node somewhere to store products for this sample
application.

### Start this application

By default the Elasticsearch cluster is supposed to run under
`localhost:9200`. You can configure the `ELASTICSEARCH_URL` environment
variable to point to another endpoint, enabling TLS, including basic auth
like this:

```
ELASTICSEARCH_URL=https://username:password@localhost:9200/
```

Either run `./gradlew bootRun` 

```
./gradlew bootRun
```

Or you package the jar and run it

```
./gradlew clean check assemble
java -jar build/libs/spring-boot-reactive-demo-0.0.1-SNAPSHOT.jar
```

## The API

### Save product

```
curl -X PUT localhost:8080/products/product/123 \
  --header "Content-Type: application/json" -d '{"name":"My Product", "description" : "A wonderful product to test out", "tags" : ["sports"], "imageUrl":"https://images.contentstack.io/v3/assets/bltefdd0b53724fa2ce/blt280217a63b82a734/5bbdaacf63ed239936a7dd56/elastic-logo.svg" }'
```

### Get product

```
curl localhost:8080/products/product/123
```

### Delete product

```
curl -X DELETE localhost:8080/products/product/123
```

### Search products

```
curl 'localhost:8080/products/search?q=sports'
```

Returns an array of JSON documents matching your query.

## HTML

### Products

Go to http://localhost:8080 to see an overview of products

