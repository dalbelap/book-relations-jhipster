'use strict';

angular.module('relacionesApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


