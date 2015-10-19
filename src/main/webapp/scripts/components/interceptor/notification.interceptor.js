 'use strict';

angular.module('relacionesApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-relacionesApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-relacionesApp-params')});
                }
                return response;
            }
        };
    });
