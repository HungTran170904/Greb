import kafkaConfig from "../configs/KafkaConfig.js";

class KafkaProducerService{
          async connect(){
                    this.producer=kafkaConfig.producer();
                    await this.producer.connect();
          }

          async produceMessage(inputLocation){
                    console.log("input location", inputLocation);
                    await this.producer.send({
                              topic: 'input-location-topic',
                              messages: [
                                        { 
                                                  key: "4e2c9def-956d-4081-a3e0-9d404f62de29", 
                                                  value: JSON.stringify(inputLocation)
                                        },
                              ]
                    })
          }

          async changeStatus(status){
                    console.log("status", status)
                    await this.producer.send({
                              topic: 'ride-event-topic',
                              messages: [
                                        { 
                                                  key: "4e2c9def-956d-4081-a3e0-9d404f62de29", 
                                                  value: status
                                        },
                              ]
                    })
          }
}
export default new KafkaProducerService();