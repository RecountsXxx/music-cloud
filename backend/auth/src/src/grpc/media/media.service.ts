import { Injectable, OnModuleInit } from '@nestjs/common';
import { ClientGrpc, Client } from '@nestjs/microservices';
import { Observable } from 'rxjs';
import { mediaGrpcClientOptions } from './media-grpc-options';
import { AvatarSetDto } from '../../auth/dtos/avatar-set.dto';

interface AvatarSetResponse {
  avatars: AvatarSetDto;
}

interface MediaService {
  getAvatars(data: { userId: string }): Observable<AvatarSetResponse>;
}

@Injectable()
export class MediaGrpcService implements OnModuleInit {
  @Client(mediaGrpcClientOptions)
  private readonly client: ClientGrpc;

  private mediaService: MediaService;

  onModuleInit() {
    this.mediaService = this.client.getService<MediaService>('MediaService');
  }

  getAvatars(userId: string): Observable<AvatarSetResponse> {
    return this.mediaService.getAvatars({ userId });
  }
}
