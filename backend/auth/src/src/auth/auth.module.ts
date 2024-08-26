import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { AuthService } from './auth.service';
import { AuthController } from './auth.controller';
import { User } from './entities/user.entity';
import { JwtStrategy } from './jwt.strategy';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { JwtAuthGuard } from './jwt-auth.guard';
import { AuthGrpcController } from '../grpc/auth/auth-grpc.controller';
import { RabbitMQService } from '../rabbitmq/rabbitmq.service';
import { RabbitMQModule } from '../rabbitmq/rabbitmq.module';
import { MediaGrpcService } from '../grpc/media/media.service';

@Module({
  imports: [
    TypeOrmModule.forFeature([User]),
    PassportModule,
    JwtModule.registerAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: async (configService: ConfigService) => ({
        secret: configService.get<string>('JWT_SECRET'),
        signOptions: { expiresIn: configService.get<string>('JWT_EXPIRES_IN') },
      }),
    }),
    RabbitMQModule,
  ],
  providers: [
    AuthService,
    RabbitMQService,
    JwtStrategy,
    JwtAuthGuard,
    MediaGrpcService,
  ],
  controllers: [AuthController, AuthGrpcController],
})
export class AuthModule {}
