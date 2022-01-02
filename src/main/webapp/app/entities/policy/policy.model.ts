import dayjs from 'dayjs/esm';
import { IAgency } from 'app/entities/agency/agency.model';
import { ICompany } from 'app/entities/company/company.model';
import { IProduct } from 'app/entities/product/product.model';
import { IPremiunDetails } from 'app/entities/premiun-details/premiun-details.model';
import { IVehicleClass } from 'app/entities/vehicle-class/vehicle-class.model';
import { IBankDetails } from 'app/entities/bank-details/bank-details.model';
import { INominee } from 'app/entities/nominee/nominee.model';
import { IMember } from 'app/entities/member/member.model';
import { IUsers } from 'app/entities/users/users.model';
import { PremiumMode } from 'app/entities/enumerations/premium-mode.model';
import { PolicyStatus } from 'app/entities/enumerations/policy-status.model';
import { Zone } from 'app/entities/enumerations/zone.model';
import { PolicyType } from 'app/entities/enumerations/policy-type.model';

export interface IPolicy {
  id?: number;
  policyAmount?: number | null;
  policyNumber?: string | null;
  term?: number | null;
  ppt?: number | null;
  commDate?: dayjs.Dayjs;
  proposerName?: string | null;
  sumAssuredAmount?: number | null;
  premiumMode?: PremiumMode | null;
  basicPremium?: number | null;
  extraPremium?: number | null;
  gst?: string | null;
  status?: PolicyStatus | null;
  totalPremiun?: string | null;
  gstFirstYear?: string | null;
  netPremium?: string | null;
  taxBeneficiary?: string | null;
  policyReceived?: boolean | null;
  previousPolicy?: number | null;
  policyStartDate?: dayjs.Dayjs | null;
  policyEndDate?: dayjs.Dayjs | null;
  period?: string | null;
  claimDone?: boolean | null;
  freeHeathCheckup?: boolean | null;
  zone?: Zone | null;
  noOfYear?: number | null;
  floaterSum?: string | null;
  tpa?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  policyType?: PolicyType | null;
  paToOwner?: string | null;
  paToOther?: string | null;
  loading?: number | null;
  riskCoveredFrom?: dayjs.Dayjs | null;
  riskCoveredTo?: dayjs.Dayjs | null;
  notes?: string | null;
  freeField1?: string | null;
  freeField2?: string | null;
  freeField3?: string | null;
  freeField4?: string | null;
  freeField5?: string | null;
  maturityDate?: dayjs.Dayjs;
  uinNo?: string | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  agency?: IAgency | null;
  company?: ICompany | null;
  product?: IProduct | null;
  premiunDetails?: IPremiunDetails | null;
  vehicleClass?: IVehicleClass | null;
  bankDetails?: IBankDetails | null;
  nominees?: INominee[] | null;
  members?: IMember[] | null;
  users?: IUsers | null;
}

export class Policy implements IPolicy {
  constructor(
    public id?: number,
    public policyAmount?: number | null,
    public policyNumber?: string | null,
    public term?: number | null,
    public ppt?: number | null,
    public commDate?: dayjs.Dayjs,
    public proposerName?: string | null,
    public sumAssuredAmount?: number | null,
    public premiumMode?: PremiumMode | null,
    public basicPremium?: number | null,
    public extraPremium?: number | null,
    public gst?: string | null,
    public status?: PolicyStatus | null,
    public totalPremiun?: string | null,
    public gstFirstYear?: string | null,
    public netPremium?: string | null,
    public taxBeneficiary?: string | null,
    public policyReceived?: boolean | null,
    public previousPolicy?: number | null,
    public policyStartDate?: dayjs.Dayjs | null,
    public policyEndDate?: dayjs.Dayjs | null,
    public period?: string | null,
    public claimDone?: boolean | null,
    public freeHeathCheckup?: boolean | null,
    public zone?: Zone | null,
    public noOfYear?: number | null,
    public floaterSum?: string | null,
    public tpa?: string | null,
    public paymentDate?: dayjs.Dayjs | null,
    public policyType?: PolicyType | null,
    public paToOwner?: string | null,
    public paToOther?: string | null,
    public loading?: number | null,
    public riskCoveredFrom?: dayjs.Dayjs | null,
    public riskCoveredTo?: dayjs.Dayjs | null,
    public notes?: string | null,
    public freeField1?: string | null,
    public freeField2?: string | null,
    public freeField3?: string | null,
    public freeField4?: string | null,
    public freeField5?: string | null,
    public maturityDate?: dayjs.Dayjs,
    public uinNo?: string | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public agency?: IAgency | null,
    public company?: ICompany | null,
    public product?: IProduct | null,
    public premiunDetails?: IPremiunDetails | null,
    public vehicleClass?: IVehicleClass | null,
    public bankDetails?: IBankDetails | null,
    public nominees?: INominee[] | null,
    public members?: IMember[] | null,
    public users?: IUsers | null
  ) {
    this.policyReceived = this.policyReceived ?? false;
    this.claimDone = this.claimDone ?? false;
    this.freeHeathCheckup = this.freeHeathCheckup ?? false;
  }
}

export function getPolicyIdentifier(policy: IPolicy): number | undefined {
  return policy.id;
}
