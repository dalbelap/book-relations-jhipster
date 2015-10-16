'use strict';

angular.module('bidarApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


