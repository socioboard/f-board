//
//  PAPPhotoCell.m
//  Anypic
//
//  Created by Héctor Ramos on 5/3/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

#import "PAPPhotoCell.h"
#import <MediaPlayer/MediaPlayer.h>

@implementation PAPPhotoCell
@synthesize photoButton,description;

#pragma mark - NSObject

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
 
    if (self) {
        // Initialization code
               self.opaque = NO;
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        self.accessoryType = UITableViewCellAccessoryNone;
        self.clipsToBounds = NO;
        
        self.backgroundColor = [UIColor clearColor];
        
        if ([reuseIdentifier isEqualToString:@"PhotoCell"]) {
            UIView *dropshadowView = [[UIView alloc] init];
            dropshadowView.backgroundColor = [UIColor whiteColor];
            dropshadowView.frame = CGRectMake( 20.0f, 0.0f,self.frame.size.width-40, 280.0f);
            [self.contentView addSubview:dropshadowView];
            
            CALayer *layer = dropshadowView.layer;
            layer.masksToBounds = NO;
            layer.shadowRadius = 3.0f;
            layer.shadowOpacity = 0.5f;
            layer.shadowOffset = CGSizeMake( 0.0f, 1.0f);
            layer.shouldRasterize = YES;
            
            self.imageView.frame = CGRectMake( 20.0f, 0.0f,self.frame.size.width-40, 280.0f);
            self.imageView.backgroundColor = [UIColor blackColor];
            self.imageView.contentMode = UIViewContentModeScaleAspectFit;
            
            self.photoButton = [UIButton buttonWithType:UIButtonTypeCustom];
            self.photoButton.frame = CGRectMake( 20.0f, 0.0f, 280.0f, 280.0f);
            self.photoButton.backgroundColor = [UIColor clearColor];
            [self.contentView addSubview:self.photoButton];
            
            [self.contentView bringSubviewToFront:self.imageView];

        }else{
            

        
            self.description=[[UILabel alloc] initWithFrame:CGRectMake( 20.0f, 13.0f,self.frame.size.width-40, 100.0f)];
            self.description.backgroundColor=[UIColor whiteColor];
            self.description.textColor=[UIColor blackColor];
            self.description.lineBreakMode=NSLineBreakByCharWrapping;
            self.description.numberOfLines=0;
//           self.description.text=@"Hello how are you";
            [self.contentView addSubview:self.description];
//            [self bringSubviewToFront:self.description];

        
        }

          }

    return self;
}


#pragma mark - UIView

- (void)layoutSubviews {
    [super layoutSubviews];
    self.imageView.frame = CGRectMake( 20.0f, 0.0f,self.frame.size.width-40, 280.0f);
    self.photoButton.frame = CGRectMake( 20.0f, 0.0f, self.frame.size.width, 280.0f);
}

@end
