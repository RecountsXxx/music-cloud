import { UserDto } from '../dtos/user.dto';
import { AvatarSetDto } from '../dtos/avatar-set.dto';

export class AuthResponse {
  user: UserDto;
  access_token: string
  avatars: AvatarSetDto;

  public constructor(user: UserDto, access_token: string, avatars: AvatarSetDto) {
    this.user = user;
    this.access_token = access_token;
    this.avatars = avatars;
  }
}