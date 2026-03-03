import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RolComponent } from './rol';

describe('Rol', () => {
  let component: RolComponent;
  let fixture: ComponentFixture<RolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RolComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RolComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
