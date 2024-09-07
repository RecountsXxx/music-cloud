import { HttpException, HttpStatus } from '@nestjs/common';

export class RefreshTokenException extends HttpException {
  constructor(response: any) {
    super(response, HttpStatus.BAD_REQUEST);
  }
}
