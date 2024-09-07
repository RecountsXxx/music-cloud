import { UserDto } from '../dtos/user.dto';

export class TokenValidationResponse {
  tokenStatus: string;
  userDTO: UserDto | null;

  public constructor(tokenStatus: string, userDTO: UserDto | null) {
    this.tokenStatus = tokenStatus;
    this.userDTO = userDTO;
  }
}
