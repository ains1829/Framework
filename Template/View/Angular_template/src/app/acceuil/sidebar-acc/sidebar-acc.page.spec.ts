import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SidebarAccPage } from './sidebar-acc.page';

describe('SidebarAccPage', () => {
  let component: SidebarAccPage;
  let fixture: ComponentFixture<SidebarAccPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(SidebarAccPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
