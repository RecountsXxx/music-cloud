import { UserDto } from '../dtos/user.dto';

export class AuthResponse {
  user: UserDto;
  access_token: string

  public constructor(user: UserDto, access_token: string) {
    this.user = user;
    this.access_token = access_token;
  }
}