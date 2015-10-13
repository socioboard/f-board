//
//  GameState.h
//  DartWheel
//
//  Created by Sumit Ghosh on 31/05/14.
//
//

#import <Foundation/Foundation.h>


@interface SingletonClass : NSObject

@property (nonatomic, assign) NSIndexPath* selectedUserIndex;
@property (nonatomic, assign) NSString* fbUserId;
//@property (nonatomic, assign) int latestScore;
@property (nonatomic,strong) NSString * accessTokenString;
//@property(nonatomic)LifeOver *lifeOver;



+(SingletonClass*)sharedState;
@end
