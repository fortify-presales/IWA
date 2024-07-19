import { Role } from './role';

export class Account {
  id: string | undefined;
  username: string | undefined;
  firstName: string | undefined;
  lastName: string | undefined;
  email: string | undefined;
  phone: string | undefined;
  address: string | undefined;
  city: string | undefined;
  state: string | undefined;
  zip: string | undefined;
  country: string | undefined;
  enabled: boolean | undefined;
  roles: Role[] | undefined;
  accessToken?: string;
  expiration?: string;

  constructor() {
    this.id = "123456";
  }
}
