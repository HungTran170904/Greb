import kafkaConfig from "../configs/KafkaConfig.js";

class KafkaProducerService{
          async connect(){
                    this.producer=kafkaConfig.producer();
                    await this.producer.connect();
          }

          async produceMessage(inputLocation){
                    console.log("input location", inputLocation);
                    await this.producer.send({
                              topic: 'input-location',
                              messages: [
                                        { 
                                                  key: "4e2c9def-956d-4081-a3e0-9d404f62de29", 
                                                  value: JSON.stringify(inputLocation)
                                        },
                              ]
                    })
          }
}
export default new KafkaProducerService();