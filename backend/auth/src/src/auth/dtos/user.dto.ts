import { User } from '../entities/user.entity';

export class UserDto {
  id: string;
  email: string;
  username: string;

  public static mapUser(user: User): UserDto {
    const { id, email, username } = user;
    return { id, email, username };
  }
}
