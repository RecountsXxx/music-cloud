export class AvatarSetDto {
  small: string;
  medium: string;
  large: string;

  public constructor(small: string, medium: string, large: string) {
    this.small = small;
    this.medium = medium;
    this.large = large;
  }
}
