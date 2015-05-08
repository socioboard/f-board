//
//  PAPPhotoCell.h
//  Anypic
//
//  Created by Héctor Ramos on 5/3/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//
#import <MediaPlayer/MediaPlayer.h>

@class PFImageView;
@interface PAPPhotoCell : UITableViewCell

@property (nonatomic, strong) UIButton *photoButton;
@property(nonatomic,strong) UILabel *description;
@property (nonatomic,strong)  UIWebView *webview;
@property (nonatomic,strong) UIView *dropshadowTextView;
//@property (nonatomic,strong) MPMoviePlayerViewController *moviePlayer;
@end
