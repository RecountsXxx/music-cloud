import { HttpException, HttpStatus } from '@nestjs/common';

export class UsernameAlreadyExistsException extends HttpException {
  constructor() {
    super('Username', HttpStatus.CONFLICT);
  }
}
