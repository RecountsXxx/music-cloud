import { UserDto } from '../dtos/user.dto';
import { AvatarSetDto } from '../dtos/avatar-set.dto';

export class AuthResponse {
  user: UserDto;
  accessToken: string;
  avatars: AvatarSetDto;

  public constructor(
    user: UserDto,
    accessToken: string,
    avatars: AvatarSetDto,
  ) {
    this.user = user;
    this.accessToken = accessToken;
    this.avatars = avatars;
  }
}
