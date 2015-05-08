//
//  ImageViewCustomCell.m
//  Anypic
//
//  Created by Socioboard 1 on 9/2/14.
//
//

#import "ImageViewCustomCell.h"
@implementation ImageViewCustomCell
@synthesize imageView,photoImgView;
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
       
        self.imageView=[[UIImageView alloc] initWithFrame:CGRectMake(5, 5, 30, 30)];
              
        [self.contentView addSubview:self.imageView];
        
        self.photoImgView=[[UIImageView alloc] initWithFrame:CGRectMake(5, 5,85, 85)];
        self.photoImgView.hidden=YES;
        [self.contentView addSubview:self.photoImgView];

        
      self.textLabel=[[UILabel alloc]initWithFrame:CGRectMake(40,0,100, 40)];
    
          self.textLabel.font=[UIFont fontWithName:@"Helvetica" size:12];
//           self.textLabel.textAlignment=NSTextAlignmentNatural;
         self.textLabel.lineBreakMode=NSLineBreakByCharWrapping;
           self.textLabel.numberOfLines=0;
         self.textLabel.backgroundColor=[UIColor clearColor];
        [self.textLabel setTextColor:[UIColor whiteColor]];
                  
          [self.contentView addSubview:self.textLabel];
//        self.checkButton=[[UIImageView alloc] initWithFrame:CGRectMake(100.0, 10.0, 20.0, 20.0)];
//        [self.contentView addSubview:self.checkButton];

        

    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
