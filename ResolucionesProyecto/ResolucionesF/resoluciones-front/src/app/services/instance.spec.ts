import { TestBed } from '@angular/core/testing';
import {InstanceService} from './instance'
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('InstanceService', () => {
  let service: InstanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(InstanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
