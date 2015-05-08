//
//  GameState.m
//  DartWheel
//
//  Created by Sumit Ghosh on 31/05/14.
//
//

#import "SingletonClass.h"

static SingletonClass *_sharedState = nil;

@implementation SingletonClass


+(SingletonClass*)sharedState {
    
    if (!_sharedState) {
        _sharedState=[[SingletonClass alloc]init];
    }
    return _sharedState;
}

-(id)init{
    
    if (self=[super init]) {
        
    }
    return self;
}
@end
