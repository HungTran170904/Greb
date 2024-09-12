import express from 'express'
import ProducerController from '../controllers/ProducerController.js';

const ProducerRouter= express.Router();

ProducerRouter.post("/test", ProducerController.testMessage);
ProducerRouter.post("/status", ProducerController.changeStatus);

export default ProducerRouter;