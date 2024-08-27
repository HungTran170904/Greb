import { config } from 'dotenv'

config({ path: `.env` })
//App
export const { APP_PORT }= process.env;

// Kafka
export const {KAFKA_BROKERS, KAFKA_CLIENT_ID}= process.env;