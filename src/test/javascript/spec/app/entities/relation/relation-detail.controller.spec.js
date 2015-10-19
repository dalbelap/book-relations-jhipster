'use strict';

describe('Relation Detail Controller', function() {
  var scope, rootScope, entity, createController;

  beforeEach(module('relacionesApp'));
  beforeEach(inject(function($rootScope, $controller) {
    rootScope = $rootScope;
    scope = rootScope.$new();
    entity = jasmine.createSpyObj('entity', ['unused']);

    createController = function() {
      return $controller("RelationDetailController", {
        '$scope': scope,
        '$rootScope': rootScope,
        'entity': null,
        'Relation' : null,
        'User' : null
      });
    };
  }));


  describe('Root Scope Listening', function() {
    it('Unregisters root scope listener upon scope destruction',
      function() {
        var eventType = 'relacionesApp:relationUpdate';

        createController();
        expect(rootScope.$$listenerCount[eventType]).toEqual(1);

        scope.$destroy();
        expect(rootScope.$$listenerCount[eventType]).toBeUndefined();
      });
  });
});
