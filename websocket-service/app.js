import express from "express";
import { APP_PORT } from "./configs/envConfig.js";
import { createServer, Server } from 'http';
import KafkaProducerService from "./services/KafkaProducerService.js";
import ProducerRouter from "./routers/ProducerRouter.js";
import ErrorMiddleware from "./middlewares/ErrorMiddleware.js";
import cookieParser from "cookie-parser";
import compression from "compression";

const app=express();
const server = createServer(app);

const io= new Server(server);
io.on("connection",(socket)=>{
          console.log("Start socket server")
})

await KafkaProducerService.connect();

app.use(compression());
app.use(express.json());
app.use(express.urlencoded({extended:true}));
app.use(cookieParser());

app.use("/weboscket-service/producer",ProducerRouter);
app.use(ErrorMiddleware);

server.listen(APP_PORT, ()=>{
          console.log(`App starts on port ${APP_PORT}`);
})
