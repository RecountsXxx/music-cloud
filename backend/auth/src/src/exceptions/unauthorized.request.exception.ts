import { HttpException, HttpStatus } from '@nestjs/common';

export class UnauthorizedRequestException extends HttpException {
  constructor() {
    super('Unauthorized access', HttpStatus.UNAUTHORIZED);
  }
}