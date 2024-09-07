import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { authGrpcClientOptions } from './grpc/auth/auth-grpc-options';
import { HttpExceptionFilter } from './filters/http-exception.filter';
import * as cookieParser from 'cookie-parser';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.enableCors({
    origin: true,
    methods: ['GET', 'HEAD', 'PUT', 'PATCH', 'POST', 'DELETE'],
    credentials: true,
  });

  app.use(cookieParser());

  app.setGlobalPrefix('api');

  app.useGlobalFilters(new HttpExceptionFilter());

  app.connectMicroservice(authGrpcClientOptions);
  await app.startAllMicroservices();

  await app.listen(3000);
}

bootstrap()
  .then(() => {
    console.log('Application started successfully');
  })
  .catch((error) => {
    console.error('Error starting application:', error);
  });
