import { Role } from './role';

export class Account {
  id: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  state: string;
  zip: string;
  country: string;
  enabled: boolean;
  roles: Role[];
  accessToken?: string;
  expiration?: string;
}
