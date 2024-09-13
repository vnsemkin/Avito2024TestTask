package io.codefresh.gradleexample.domain.service;

import io.codefresh.gradleexample.application.dtos.PageRequestByUsername;
import io.codefresh.gradleexample.application.dtos.ReqByUserName;
import io.codefresh.gradleexample.application.exceptions.UserNotFoundException;
import io.codefresh.gradleexample.application.repositories.EmployeeRepository;
import io.codefresh.gradleexample.infrastructure.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppService {
    private final static String USERNAME_NOT_FOUND = "User with username %s not found";
    private final EmployeeRepository employeeRepository;

    public PageRequestByUsername getPageRequestIfUserExist(@NonNull ReqByUserName reqByUserName) {
        Sort sort = Sort.by(Sort.Direction.fromString(reqByUserName.sortDirection()), reqByUserName.sortField());
        PageRequest pageRequest =
                PageRequest.of(reqByUserName.offset() / reqByUserName.limit(), reqByUserName.limit(), sort);

        Employee employee = employeeRepository.findByUsername(reqByUserName.username())
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(USERNAME_NOT_FOUND, reqByUserName.username())));
        return new PageRequestByUsername(pageRequest, employee);
    }
}
