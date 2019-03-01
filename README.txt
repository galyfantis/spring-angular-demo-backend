docker volume create derbyfs

docker network create demonet

docker run -ti -v derbyfs:/database -p 8888:1527 --name derby-db --network demonet derby-db-service:0.0.1-SNAPSHOT

docker run -ti -e "JAVA_OPTS=-Dapp.derby.host=derby-db:1527" -p 8090:8080 --name backend-app --network demonet spring-boot-demo:0.0.1-SNAPSHOT
