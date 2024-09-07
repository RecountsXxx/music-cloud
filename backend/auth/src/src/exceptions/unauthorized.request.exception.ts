import { HttpException, HttpStatus } from '@nestjs/common';

export class UnauthorizedRequestException extends HttpException {
  constructor(message: string = 'Unauthorized access') {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
