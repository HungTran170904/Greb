import { Kafka } from "kafkajs";
import { KAFKA_BROKERS, KAFKA_CLIENT_ID } from "./envConfig.js";

console.log("Kafka brokers", KAFKA_BROKERS)
console.log("client id", KAFKA_CLIENT_ID)
const kafkaConfig=new Kafka({
          clientId: KAFKA_CLIENT_ID,
          brokers: KAFKA_BROKERS.split(",")
})
export default kafkaConfig;