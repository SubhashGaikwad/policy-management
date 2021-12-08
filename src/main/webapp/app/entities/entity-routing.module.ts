import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'policy',
        data: { pageTitle: 'policyManagementApp.policy.home.title' },
        loadChildren: () => import('./policy/policy.module').then(m => m.PolicyModule),
      },
      {
        path: 'nominee',
        data: { pageTitle: 'policyManagementApp.nominee.home.title' },
        loadChildren: () => import('./nominee/nominee.module').then(m => m.NomineeModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'policyManagementApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'product-details',
        data: { pageTitle: 'policyManagementApp.productDetails.home.title' },
        loadChildren: () => import('./product-details/product-details.module').then(m => m.ProductDetailsModule),
      },
      {
        path: 'product-type',
        data: { pageTitle: 'policyManagementApp.productType.home.title' },
        loadChildren: () => import('./product-type/product-type.module').then(m => m.ProductTypeModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'policyManagementApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'company-type',
        data: { pageTitle: 'policyManagementApp.companyType.home.title' },
        loadChildren: () => import('./company-type/company-type.module').then(m => m.CompanyTypeModule),
      },
      {
        path: 'users',
        data: { pageTitle: 'policyManagementApp.users.home.title' },
        loadChildren: () => import('./users/users.module').then(m => m.UsersModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'policyManagementApp.address.home.title' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'users-type',
        data: { pageTitle: 'policyManagementApp.usersType.home.title' },
        loadChildren: () => import('./users-type/users-type.module').then(m => m.UsersTypeModule),
      },
      {
        path: 'security-user',
        data: { pageTitle: 'policyManagementApp.securityUser.home.title' },
        loadChildren: () => import('./security-user/security-user.module').then(m => m.SecurityUserModule),
      },
      {
        path: 'user-access',
        data: { pageTitle: 'policyManagementApp.userAccess.home.title' },
        loadChildren: () => import('./user-access/user-access.module').then(m => m.UserAccessModule),
      },
      {
        path: 'security-role',
        data: { pageTitle: 'policyManagementApp.securityRole.home.title' },
        loadChildren: () => import('./security-role/security-role.module').then(m => m.SecurityRoleModule),
      },
      {
        path: 'security-permission',
        data: { pageTitle: 'policyManagementApp.securityPermission.home.title' },
        loadChildren: () => import('./security-permission/security-permission.module').then(m => m.SecurityPermissionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
