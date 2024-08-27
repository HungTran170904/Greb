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
}
export default new ProducerController();