import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { MicroserviceOptions } from '@nestjs/microservices';
import { rabbitmqOptions } from './rabbitmq/rabbitmq.options';
import { ConfigService } from '@nestjs/config';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const configService = app.get(ConfigService);

  app.connectMicroservice<MicroserviceOptions>(rabbitmqOptions(configService));

  await app.startAllMicroservices();
  await app.listen(3001);
}

bootstrap()
  .then(() => {
    console.log('Application started successfully');
  })
  .catch((error) => {
    console.error('Error starting application:', error);
  });
