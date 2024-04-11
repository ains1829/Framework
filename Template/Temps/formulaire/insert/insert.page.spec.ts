import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InsertPage } from './insert.page';

describe('InsertPage', () => {
  let component: InsertPage;
  let fixture: ComponentFixture<InsertPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(InsertPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
