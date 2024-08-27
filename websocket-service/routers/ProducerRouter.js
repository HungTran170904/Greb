import express from 'express'
import ProducerController from '../controllers/ProducerController.js';

const ProducerRouter= express.Router();

ProducerRouter.post("/test", ProducerController.testMessage);

export default ProducerRouter;