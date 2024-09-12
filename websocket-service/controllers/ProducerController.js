import KafkaProducerService from "../services/KafkaProducerService.js";

class ProducerController{
          async testMessage(req,res,next){
                    try{
                              await KafkaProducerService.produceMessage(req.body);
                              return res.status(204).end();
                    }
                    catch(error){
                              next(error);
                    }
          }

          async changeStatus(req,res,next){
                    try{
                              console.log(req.query)
                              await KafkaProducerService.changeStatus(req.query["status"])
                              return res.status(204).end();
                    }
                    catch(error){
                              next(error);
                    }
          }
}
export default new ProducerController();