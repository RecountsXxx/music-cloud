export class UserDto {
  id: string;
  email: string;
  username: string;

  public constructor(id: string, email: string, username: string) {
    this.id = id;
    this.email = email;
    this.username = username;
  }
}
